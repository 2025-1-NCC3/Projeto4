const express = require("express");
const cors = require("cors");
const connection = require("./db");

const app = express();
app.use(cors());
app.use(express.json());

app.get("/", (req, res) => {
    res.send("API está rodando!");
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Servidor rodando na porta ${PORT}`);
});

const usuarioRoutes = require("./routes/usuarios");
app.use("/usuarios", usuarioRoutes);

