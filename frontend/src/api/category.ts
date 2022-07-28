import { CategoriesGetResponseType, CategoryType } from '@/@types/category';

import dallogApi from './';

const categoryApi = {
  endpoint: {
    entire: '/api/categories',
    my: '/api/categories/me',
  },
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  getEntire: async (page: number, size: number) => {
    const response = await dallogApi.get<CategoriesGetResponseType>(categoryApi.endpoint.entire, {
      params: { page, size },
      headers: categoryApi.headers,
    });

    return response;
  },

  getMy: async (accessToken: string | null) => {
    const response = await dallogApi.get<CategoryType[]>(categoryApi.endpoint.my, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
      transformResponse: (res) => JSON.parse(res).categories,
    });

    return response;
  },

  post: async (accessToken: string | null, body: Pick<CategoryType, 'name'>) => {
    const response = await dallogApi.post(categoryApi.endpoint.entire, body, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default categoryApi;
