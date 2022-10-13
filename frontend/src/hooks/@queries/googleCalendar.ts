import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { GoogleCalendarGetResponseType, GoogleCalendarPostBodyType } from '@/@types/googleCalendar';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants/api';

import googleCalendarApi from '@/api/googleCalendar';

interface UsePostGoogleCalendarCategoryParams {
  onSuccess?: () => void;
}

function useGetGoogleCalendar() {
  const { accessToken } = useRecoilValue(userState);

  const { isLoading, data } = useQuery<AxiosResponse<GoogleCalendarGetResponseType>, AxiosError>(
    CACHE_KEY.GOOGLE_CALENDAR,
    () => googleCalendarApi.get(accessToken)
  );

  return { isLoading, data };
}

function usePostGoogleCalendarCategory({ onSuccess }: UsePostGoogleCalendarCategoryParams) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation(
    (body: GoogleCalendarPostBodyType) => googleCalendarApi.post(accessToken, body),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
        queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
        queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
        queryClient.invalidateQueries(CACHE_KEY.SCHEDULES);

        onSuccess && onSuccess();
      },
    }
  );

  return { mutate };
}

export { useGetGoogleCalendar, usePostGoogleCalendarCategory };
