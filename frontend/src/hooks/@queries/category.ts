import { AxiosError, AxiosResponse } from 'axios';
import { useInfiniteQuery, useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import {
  CategoriesGetResponseType,
  CategoryRoleType,
  CategorySubscriberType,
  CategoryType,
} from '@/@types/category';

import { userState } from '@/recoil/atoms';

import { API, CACHE_KEY } from '@/constants/api';

import categoryApi from '@/api/category';

interface useDeleteCategoryProps {
  categoryId: number;
  onSuccess?: () => void;
}

interface useGetAdminCategoriesProps {
  enabled?: boolean;
}

interface useGetEditableCategoriesProps {
  enabled?: boolean;
}

interface useGetEntireCategoriesProps {
  keyword: string;
}

interface useGetSingleCategoryProps {
  categoryId: number;
}

interface useGetSubscribersProps {
  categoryId: number;
}

interface usePatchCategoryRoleProps {
  categoryId: number;
  memberId: number;
  onSuccess?: () => void;
}

interface usePostCategoryProps {
  onSuccess?: () => void;
}

interface usePatchCategoryNameProps {
  categoryId: number;
  onSuccess?: () => void;
}

function useDeleteCategory({ categoryId, onSuccess }: useDeleteCategoryProps) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation(() => categoryApi.delete(accessToken, categoryId), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
      queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);

      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

function useGetAdminCategories({ enabled }: useGetAdminCategoriesProps) {
  const { accessToken } = useRecoilValue(userState);

  const { isLoading, data } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
    [CACHE_KEY.ADMIN_CATEGORIES],
    () => categoryApi.getAdmin(accessToken),
    {
      enabled,
    }
  );

  return { isLoading, data };
}

function useGetEditableCategories({ enabled }: useGetEditableCategoriesProps) {
  const { accessToken } = useRecoilValue(userState);

  const { isLoading, data } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
    [CACHE_KEY.EDITABLE_CATEGORIES],
    () => categoryApi.getEditable(accessToken),
    {
      enabled,
    }
  );

  return { isLoading, data };
}

function useGetEntireCategories({ keyword }: useGetEntireCategoriesProps) {
  const { error, data, fetchNextPage, hasNextPage } = useInfiniteQuery<
    AxiosResponse<CategoriesGetResponseType>,
    AxiosError
  >(
    [CACHE_KEY.CATEGORIES, keyword],
    ({ pageParam = 0 }) => categoryApi.getEntire(keyword, pageParam, API.CATEGORY_GET_SIZE),
    {
      getNextPageParam: ({ data }) => {
        if (data.categories.length > 0) {
          return data.page + 1;
        }
      },
    }
  );

  return { error, data, fetchNextPage, hasNextPage };
}

function useGetMyCategories() {
  const { accessToken } = useRecoilValue(userState);

  const { data } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
    CACHE_KEY.MY_CATEGORIES,
    () => categoryApi.getMy(accessToken)
  );

  return { data };
}

function useGetSingleCategory({ categoryId }: useGetSingleCategoryProps) {
  const { data } = useQuery<AxiosResponse<CategoryType>, AxiosError>(CACHE_KEY.CATEGORY, () =>
    categoryApi.getSingle(categoryId)
  );

  return { data };
}

function useGetSubscribers({ categoryId }: useGetSubscribersProps) {
  const { accessToken } = useRecoilValue(userState);

  const { isLoading, data } = useQuery<AxiosResponse<CategorySubscriberType[]>, AxiosError>(
    [CACHE_KEY.SUBSCRIBERS, categoryId],
    () => categoryApi.getSubscribers(accessToken, categoryId)
  );

  return { isLoading, data };
}

function usePatchCategoryName({ categoryId, onSuccess }: usePatchCategoryNameProps) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation<
    AxiosResponse<Pick<CategoryType, 'name'>>,
    AxiosError,
    Pick<CategoryType, 'name'>,
    unknown
  >((body) => categoryApi.patch(accessToken, categoryId, body), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
      queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);

      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

function usePatchCategoryRole({ categoryId, memberId, onSuccess }: usePatchCategoryRoleProps) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation<
    AxiosResponse<{ categoryRoleType: CategoryRoleType }>,
    AxiosError,
    { categoryRoleType: CategoryRoleType },
    unknown
  >((body) => categoryApi.patchRole(accessToken, categoryId, memberId, body), {
    onSuccess: () => {
      queryClient.invalidateQueries([CACHE_KEY.SUBSCRIBERS]);
      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

function usePostCategory({ onSuccess }: usePostCategoryProps) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation<
    AxiosResponse<CategoryType>,
    AxiosError,
    Pick<CategoryType, 'name' | 'categoryType'>,
    unknown
  >((body) => categoryApi.post(accessToken, body), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
      queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);

      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

export {
  useDeleteCategory,
  useGetAdminCategories,
  useGetEditableCategories,
  useGetEntireCategories,
  useGetMyCategories,
  useGetSingleCategory,
  useGetSubscribers,
  usePatchCategoryName,
  usePatchCategoryRole,
  usePostCategory,
};
