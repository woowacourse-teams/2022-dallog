interface CategoryType {
  id: number;
  name: string;
  createdAt: string;
}

interface CategoriesGetResponseType {
  page: number;
  categories: CategoryType[];
}

export { CategoryType, CategoriesGetResponseType };
