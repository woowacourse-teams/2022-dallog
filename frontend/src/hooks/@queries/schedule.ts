import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { ScheduleResponseType, ScheduleType } from '@/@types/schedule';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants/api';

import scheduleApi from '@/api/schedule';

interface UseGetSchedulesParams {
  startDateTime: string;
  endDateTime: string;
}

interface UseDeleteScheduleParams {
  scheduleId: string;
  onSuccess?: () => void;
}

interface UsePatchScheduleParams {
  scheduleId: string;
  onSuccess?: () => void;
}

interface UsePostScheduleParams {
  categoryId: string;
  onSuccess?: () => void;
}

function useDeleteSchedule({ scheduleId, onSuccess }: UseDeleteScheduleParams) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation<AxiosResponse, AxiosError>(
    () => scheduleApi.delete(accessToken, scheduleId),
    {
      onSuccess: () => {
        queryClient.invalidateQueries([CACHE_KEY.SCHEDULES]);
        onSuccess && onSuccess();
      },
    }
  );

  return { mutate };
}

function useGetSchedules({ startDateTime, endDateTime }: UseGetSchedulesParams) {
  const { accessToken } = useRecoilValue(userState);

  const { isLoading, data } = useQuery<AxiosResponse<ScheduleResponseType>, AxiosError>(
    [CACHE_KEY.SCHEDULES, startDateTime, endDateTime],
    () => scheduleApi.get(accessToken, startDateTime, endDateTime)
  );

  return {
    isLoading,
    data,
  };
}

function usePatchSchedule({ scheduleId, onSuccess }: UsePatchScheduleParams) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation<
    AxiosResponse,
    AxiosError,
    Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode' | 'categoryType'>,
    unknown
  >(CACHE_KEY.SCHEDULE, (body) => scheduleApi.patch(accessToken, scheduleId, body), {
    onSuccess: () => {
      queryClient.invalidateQueries([CACHE_KEY.SCHEDULES]);
      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

function usePostSchedule({ categoryId, onSuccess }: UsePostScheduleParams) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation<
    AxiosResponse<{ schedules: ScheduleType[] }>,
    AxiosError,
    Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode' | 'categoryType'>,
    unknown
  >((body) => scheduleApi.post(accessToken, Number(categoryId), body), {
    onSuccess: () => {
      queryClient.invalidateQueries([CACHE_KEY.SCHEDULES]);
      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

export { useDeleteSchedule, useGetSchedules, usePatchSchedule, usePostSchedule };
