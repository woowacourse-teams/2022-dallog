import axios from 'axios';

const dallogApi = axios.create({
  baseURL: process.env.API_URL,
});

export default dallogApi;
