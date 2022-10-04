import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { ScheduleResponseType, ScheduleType } from '@/@types/schedule';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants/api';

import scheduleApi from '@/api/schedule';

interface useGetSchedulesProps {
  startDateTime: string;
  endDateTime: string;
}

interface useDeleteScheduleProps {
  scheduleId: string;
  onSuccess?: () => void;
}

interface usePatchScheduleProps {
  scheduleId: string;
  onSuccess?: () => void;
}

interface usePostScheduleProps {
  categoryId: string;
  onSuccess?: () => void;
}

function useDeleteSchedule({ scheduleId, onSuccess }: useDeleteScheduleProps) {
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

function useGetSchedules({ startDateTime, endDateTime }: useGetSchedulesProps) {
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

function usePatchSchedule({ scheduleId, onSuccess }: usePatchScheduleProps) {
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

function usePostSchedule({ categoryId, onSuccess }: usePostScheduleProps) {
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
