const axios = require("axios");
const { login, refresh } = require("../../routes/webhooks/auth/authWebhooks");

let jwtToken = null;
let refreshToken = null;

let data = null;

let expireDateAccessToken = new Date();

let expireDateRefreshToken = new Date();

let baseURL = process.env.QUARKUS_BASEURL || `${QUARKUS_BASEURL}`;
let quarkusLogin = process.env.API_BACKEND_LOGIN || `${API_BACKEND_LOGIN}`;
let quarkusPassword =
  process.env.API_BACKEND_PASSWORD || `${API_BACKEND_PASSWORD}`;

const api = axios.create({
  baseURL: baseURL,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(async (config) => {
  if (data && Date.parse(expireDateAccessToken) < Date.now()) {
    try {
      await refreshAuthenticate(data.refreshToken);
      console.log("Token atualizado com sucesso");
    } catch (error) {
      console.error("Erro ao atualizar o token JWT:", error.message);
      throw new Error("Falha ao atualizar o token JWT");
    }
  }

  if (jwtToken) {
    config.headers.Authorization = `Bearer ${jwtToken}`;
  }

  return config;
});

async function authenticate() {
  try {
    const response = await login(api, quarkusLogin, quarkusPassword);
    const responseData = response?.data?.data;
    jwtToken = responseData.accessToken;
    refreshToken = responseData.refreshToken;
    expireDateAccessToken = responseData.expireDateAccessToken;
    expireDateRefreshToken = responseData.expireDateRefreshToken;
    data = responseData;
    api.defaults.headers.common["Authorization"] = `Bearer ${jwtToken}`;

    console.log("Token JWT autenticado com sucesso!");
  } catch (error) {
    console.error("Erro ao autenticar:", error.message);
    throw new Error("Falha na autenticação");
  }
}

async function refreshAuthenticate(refreshToken) {
  try {
    const response = await refresh(api, refreshToken);
    const responseData = response?.data?.data;
    jwtToken = responseData.accessToken;
    refreshToken = responseData.refreshToken;
    expireDateAccessToken = responseData.expireDateAccessToken;
    expireDateRefreshToken = responseData.expireDateRefreshToken;
    data = responseData;

    api.defaults.headers.common["Authorization"] = `Bearer ${jwtToken}`;

    console.log("Token JWT atualizado com sucesso!");
  } catch (error) {
    console.error("Erro ao atualizar o token JWT:", error.message);
    throw new Error("Falha ao atualizar o token JWT");
  }
}
module.exports = {
  api,
  authenticate,
  refreshAuthenticate,
};
