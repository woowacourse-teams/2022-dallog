import { AxiosError, AxiosResponse } from 'axios';
import { QueryKey, useMutation, useQuery, useQueryClient, UseQueryOptions } from 'react-query';
import { useRecoilValue } from 'recoil';

import useSnackBar from '@/hooks/useSnackBar';

import {
  CategoryRoleType,
  CategorySubscriberType,
  CategoryType,
  SingleCategoryType,
} from '@/@types/category';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants/api';
import { SUCCESS_MESSAGE } from '@/constants/message';

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

interface UseGetSingleCategoryParams
  extends Omit<
    UseQueryOptions<
      AxiosResponse<SingleCategoryType>,
      AxiosError<unknown>,
      AxiosResponse<SingleCategoryType>,
      QueryKey
    >,
    'queryKey' | 'queryFn'
  > {
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
  const { openSnackBar } = useSnackBar();

  const { mutate } = useMutation(() => categoryApi.delete(accessToken, categoryId), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
      queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);

      openSnackBar(SUCCESS_MESSAGE.DELETE_CATEGORY);
      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

function useGetAdminCategories({ enabled }: UseGetAdminCategoriesParams) {
  const { accessToken } = useRecoilValue(userState);

  const { isLoading, data } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
    CACHE_KEY.ADMIN_CATEGORIES,
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
    CACHE_KEY.EDITABLE_CATEGORIES,
    () => categoryApi.getEditable(accessToken),
    {
      enabled,
      suspense: true,
    }
  );

  return { isLoading, data };
}

function useGetEntireCategories({ keyword }: UseGetEntireCategoriesParams) {
  const { data } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
    [CACHE_KEY.CATEGORIES, keyword],
    () => categoryApi.getEntire(keyword),
    {
      suspense: true,
    }
  );

  return { data };
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
    [CACHE_KEY.SCHEDULES, categoryId, startDateTime, endDateTime],
    () => categoryApi.getSchedules(categoryId, startDateTime, endDateTime),
    {
      enabled: !!categoryId,
    }
  );

  return { isLoading, data };
}

function useGetSingleCategory({ categoryId, ...options }: UseGetSingleCategoryParams) {
  const { data } = useQuery<AxiosResponse<SingleCategoryType>, AxiosError>(
    [CACHE_KEY.CATEGORY, categoryId],
    () => categoryApi.getSingle(categoryId),
    { ...options }
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
  const { openSnackBar } = useSnackBar();

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

      openSnackBar(SUCCESS_MESSAGE.PATCH_CATEGORY_NAME);
      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

function usePatchCategoryRole({ categoryId, memberId, onSuccess }: UsePatchCategoryRoleParams) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();
  const { openSnackBar } = useSnackBar();

  const { mutate } = useMutation<
    AxiosResponse<{ categoryRoleType: CategoryRoleType }>,
    AxiosError,
    { categoryRoleType: CategoryRoleType },
    unknown
  >((body) => categoryApi.patchRole(accessToken, categoryId, memberId, body), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIBERS);
      queryClient.invalidateQueries(CACHE_KEY.EDITABLE_CATEGORIES);
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);

      openSnackBar(SUCCESS_MESSAGE.PATCH_CATEGORY_ROLE);
      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

function usePostCategory({ onSuccess }: UsePostCategoryParams) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();
  const { openSnackBar } = useSnackBar();

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
      queryClient.invalidateQueries(CACHE_KEY.EDITABLE_CATEGORIES);

      openSnackBar(SUCCESS_MESSAGE.POST_CATEGORY);
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
