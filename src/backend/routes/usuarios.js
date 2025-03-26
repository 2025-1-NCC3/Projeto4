const express = require("express");
const axios = require("axios");
const bcrypt = require("bcryptjs");
const connection = require("../db");

const router = express.Router();
const API_URL = "https://c3lczp-3000.csb.app"; // Link do CodeSandbox

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
      async (err, result) => {
        if (err) {
          console.error(err);
          return res.status(500).json({ error: "Erro ao cadastrar!" });
        }
        // Enviar requisição ao backend do CodeSandbox após o cadastro
        try {
          const response = await axios.post(`${API_URL}/usuarios/cadastro`, {
            nome,
            email,
            telefone,
            senha,
          });
          console.log(response.data); // Aqui você pode ver a resposta da API do CodeSandbox
          res.status(201).json({ message: "Usuário cadastrado com sucesso!" });
        } catch (error) {
          console.error(error);
          res
            .status(500)
            .json({ error: "Erro ao fazer requisição para o CodeSandbox!" });
        }
      }
    );
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Erro interno no servidor!" });
  }
});

module.exports = router;


