require("dotenv").config();
const express = require("express");
const mysql = require("mysql2");
const cors = require("cors");

const app = express();
app.use(express.json());
app.use(cors());

// Conexão com o MySQL
const db = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME
});

db.connect(err => {
    if (err) {
        console.error("Erro ao conectar ao MySQL:", err);
    } else {
        console.log("Conectado ao MySQL!");
    }
});

// Criar um registro (POST)
app.post("/registros", (req, res) => {
    const { usuario_id, sentimento, localizacao } = req.body;
    const sql = "INSERT INTO registros (usuario_id, sentimento, localizacao) VALUES (?, ?, ?)";
    db.query(sql, [usuario_id, sentimento, localizacao], (err, result) => {
        if (err) return res.status(500).json(err);
        res.status(201).json({ id: result.insertId, usuario_id, sentimento, localizacao });
    });
});

// Ler todos os registros (GET)
app.get("/registros", (req, res) => {
    db.query("SELECT * FROM registros", (err, results) => {
        if (err) return res.status(500).json(err);
        res.json(results);
    });
});

// Atualizar um registro (PUT)
app.put("/registros/:id", (req, res) => {
    const { id } = req.params;
    const { sentimento, localizacao } = req.body;
    const sql = "UPDATE registros SET sentimento = ?, localizacao = ? WHERE id = ?";
    db.query(sql, [sentimento, localizacao, id], (err, result) => {
        if (err) return res.status(500).json(err);
        res.json({ message: "Registro atualizado", id });
    });
});

// Deletar um registro (DELETE)
app.delete("/registros/:id", (req, res) => {
    const { id } = req.params;
    const sql = "DELETE FROM registros WHERE id = ?";
    db.query(sql, [id], (err, result) => {
        if (err) return res.status(500).json(err);
        res.json({ message: "Registro deletado", id });
    });
});

// Iniciar o servidor
app.listen(3000, () => console.log("Servidor rodando na porta 3000"));