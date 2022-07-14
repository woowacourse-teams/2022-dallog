interface CategoryType {
  id: number;
  name: string;
  createdAt: string;
}

interface CategoriesGetResponseType {
  totalCount: number;
  page: number;
  data: CategoryType[];
}

export { CategoryType, CategoriesGetResponseType };
