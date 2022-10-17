import { useMutation, useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import useSnackBar from '@/hooks/useSnackBar';

import { userState } from '@/recoil/atoms';

import { PATH } from '@/constants';
import { CACHE_KEY } from '@/constants/api';
import { SUCCESS_MESSAGE } from '@/constants/message';

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
  const { openSnackBar } = useSnackBar();

  const { mutate } = useMutation(() => profileApi.delete(accessToken), {
    onSuccess: () => {
      onSuccess && onSuccess();
      removeAccessToken();
      removeRefreshToken();
      navigate(PATH.MAIN);
      location.reload();
      openSnackBar(SUCCESS_MESSAGE.DELETE_PROFILE);
    },
  });

  return { mutate };
}

function usePatchProfile({ accessToken }: UsePatchProfileParams) {
  const queryClient = useQueryClient();
  const { openSnackBar } = useSnackBar();

  const { mutate } = useMutation(
    (body: { displayName: string }) => profileApi.patch(accessToken, body),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(CACHE_KEY.PROFILE);
        queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);

        openSnackBar(SUCCESS_MESSAGE.PATCH_PROFILE_NAME);
      },
    }
  );

  return { mutate };
}

export { useDeleteProfile, usePatchProfile };
