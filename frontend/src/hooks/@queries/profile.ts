import { useMutation, useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { userState } from '@/recoil/atoms';

import { PATH } from '@/constants';
import { CACHE_KEY } from '@/constants/api';

import { removeAccessToken, removeRefreshToken } from '@/utils/storage';

import profileApi from '@/api/profile';

interface UseDeleteProfileParams {
  onSuccess?: () => void;
}

interface UsePatchProfileParams {
  accessToken: string;
}

function useDeleteProfile({ onSuccess }: UseDeleteProfileParams) {
  const navigate = useNavigate();

  const { accessToken } = useRecoilValue(userState);

  const { mutate } = useMutation(() => profileApi.delete(accessToken), {
    onSuccess: () => {
      onSuccess && onSuccess();
      removeAccessToken();
      removeRefreshToken();
      navigate(PATH.MAIN);
      location.reload();
    },
  });

  return { mutate };
}

function usePatchProfile({ accessToken }: UsePatchProfileParams) {
  const queryClient = useQueryClient();

  const { mutate } = useMutation(
    (body: { displayName: string }) => profileApi.patch(accessToken, body),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(CACHE_KEY.PROFILE);
        queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
      },
    }
  );

  return { mutate };
}

export { useDeleteProfile, usePatchProfile };
