const express = require("express");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const connection = require("../db");

const router = express.Router();

// Cadastro de usuário
router.post("/cadastro", async (req, res) => {
    const { nome, email, telefone, senha } = req.body;

    if (!nome || !email || !telefone || !senha) {
        return res.status(400).json({ error: "Preencha todos os campos!" });
    }

    try {
        const senhaHash = await bcrypt.hash(senha, 10);

        connection.query(
            "INSERT INTO usuarios (nome, email, telefone, senha) VALUES (?, ?, ?, ?)",
            [nome, email, telefone, senhaHash],
            (err, result) => {
                if (err) {
                    console.error(err);
                    return res.status(500).json({ error: "Erro ao cadastrar!" });
                }
                res.status(201).json({ message: "Usuário cadastrado com sucesso!" });
            }
        );
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: "Erro interno no servidor!" });
    }
});
// Login do usuário
router.post("/login", (req, res) => {
    const { email, senha } = req.body;

    connection.query(
        "SELECT * FROM usuarios WHERE email = ?",
        [email],
        async (err, results) => {
            if (err) {
                return res.status(500).json({ error: "Erro no servidor!" });
            }

            if (results.length === 0) {
                return res.status(401).json({ error: "Usuário não encontrado!" });
            }

            const usuario = results[0];
            const senhaCorreta = await bcrypt.compare(senha, usuario.senha);

            if (!senhaCorreta) {
                return res.status(401).json({ error: "Senha incorreta!" });
            }

            const token = jwt.sign(
                { id: usuario.id, email: usuario.email, nome: usuario.nome, telefone: usuario.telefone },
                process.env.JWT_SECRET,
                { expiresIn: "1h" }
            );

            res.json({ token, nome: usuario.nome, telefone: usuario.telefone, message: "Login realizado com sucesso!" });
        }
    );
});
module.exports = router;

