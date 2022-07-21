import axios from 'axios';

import { CategoriesGetResponseType, CategoryType } from '@/@types/category';

import { API_KEY } from '@/constants';

const categoryApi = {
  endpoint: `${API_KEY}/api/categories`,
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

  post: async (accessToken: string | null, body: Pick<CategoryType, 'name'>) => {
    const response = await axios.post(categoryApi.endpoint, body, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default categoryApi;
