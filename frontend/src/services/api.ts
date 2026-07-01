import axios from 'axios'

/**
 * Fichier de configuration du client HTTP (Axios).
 * 
 * Centraliser la configuration Axios ici permet 
 * de ne pas avoir à réécrire 'http://localhost:8080/api' partout dans le code.
 */
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  }
})

export default api
