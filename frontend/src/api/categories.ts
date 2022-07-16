import axios from 'axios';

import { CategoriesGetResponseType } from '@/@types/category';

const categoryApi = {
  endpoint: '/api/categories',
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (page: number, size: number) => {
    const response = await axios.get<CategoriesGetResponseType>(categoryApi.endpoint, {
      params: { page, size },
      headers: categoryApi.headers,
    });

    return response;
  },
};

export default categoryApi;
