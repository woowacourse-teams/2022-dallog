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
      transformResponse: (res) => {
        const categories = JSON.parse(res);
        return categories;
      },
    });

    return response;
  },
};

export default categoryApi;
