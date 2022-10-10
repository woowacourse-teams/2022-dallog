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

interface UseDeleteCategoryParams {
  categoryId: number;
  onSuccess?: () => void;
}

interface UseGetAdminCategoriesParams {
  enabled?: boolean;
}

interface UseGetEditableCategoriesParams {
  enabled?: boolean;
}

interface UseGetEntireCategoriesParams {
  keyword: string;
}

interface UseGetSchedulesWithCategoryParams {
  categoryId: number;
  startDateTime: string;
  endDateTime: string;
}

interface UseGetSingleCategoryParams {
  categoryId: number;
}

interface UseGetSubscribersParams {
  categoryId: number;
}

interface UsePatchCategoryRoleParams {
  categoryId: number;
  memberId: number;
  onSuccess?: () => void;
}

interface UsePatchCategoryNameParams {
  categoryId: number;
  onSuccess?: () => void;
}

interface UsePostCategoryParams {
  onSuccess?: () => void;
}

function useDeleteCategory({ categoryId, onSuccess }: UseDeleteCategoryParams) {
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

function useGetAdminCategories({ enabled }: UseGetAdminCategoriesParams) {
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

function useGetEditableCategories({ enabled }: UseGetEditableCategoriesParams) {
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

function useGetEntireCategories({ keyword }: UseGetEntireCategoriesParams) {
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

  const { isLoading, data } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
    CACHE_KEY.MY_CATEGORIES,
    () => categoryApi.getMy(accessToken)
  );

  return { isLoading, data };
}

function useGetSchedulesWithCategory({
  categoryId,
  startDateTime,
  endDateTime,
}: UseGetSchedulesWithCategoryParams) {
  const { isLoading, data } = useQuery(
    [CACHE_KEY.SCHEDULES, categoryId],
    () => categoryApi.getSchedules(categoryId, startDateTime, endDateTime),
    {
      enabled: !!categoryId,
    }
  );

  return { isLoading, data };
}

function useGetSingleCategory({ categoryId }: UseGetSingleCategoryParams) {
  const { data } = useQuery<AxiosResponse<CategoryType>, AxiosError>(CACHE_KEY.CATEGORY, () =>
    categoryApi.getSingle(categoryId)
  );

  return { data };
}

function useGetSubscribers({ categoryId }: UseGetSubscribersParams) {
  const { accessToken } = useRecoilValue(userState);

  const { isLoading, data } = useQuery<AxiosResponse<CategorySubscriberType[]>, AxiosError>(
    [CACHE_KEY.SUBSCRIBERS, categoryId],
    () => categoryApi.getSubscribers(accessToken, categoryId)
  );

  return { isLoading, data };
}

function usePatchCategoryName({ categoryId, onSuccess }: UsePatchCategoryNameParams) {
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

function usePatchCategoryRole({ categoryId, memberId, onSuccess }: UsePatchCategoryRoleParams) {
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

function usePostCategory({ onSuccess }: UsePostCategoryParams) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation<
    AxiosResponse<CategoryType>,
    AxiosError,
    Pick<CategoryType, 'name' | 'categoryType'>,
    unknown
  >((body) => categoryApi.post(accessToken, body), {
    onSuccess: () => {
      queryClient.invalidateQueries([CACHE_KEY.CATEGORIES]);
      queryClient.invalidateQueries([CACHE_KEY.MY_CATEGORIES]);
      queryClient.invalidateQueries([CACHE_KEY.SUBSCRIPTIONS]);
      queryClient.invalidateQueries([CACHE_KEY.EDITABLE_CATEGORIES]);

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
  useGetSchedulesWithCategory,
  useGetSingleCategory,
  useGetSubscribers,
  usePatchCategoryName,
  usePatchCategoryRole,
  usePostCategory,
};
