import { useMutation, useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { userState } from '@/recoil/atoms';

import { PATH } from '@/constants';
import { CACHE_KEY } from '@/constants/api';

import { removeAccessToken } from '@/utils/storage';

import profileApi from '@/api/profile';

interface useDeleteProfileProps {
  onSuccess?: () => void;
}

interface usePatchProfileProps {
  accessToken: string;
}

function useDeleteProfile({ onSuccess }: useDeleteProfileProps) {
  const navigate = useNavigate();

  const { accessToken } = useRecoilValue(userState);

  const { mutate } = useMutation(() => profileApi.delete(accessToken), {
    onSuccess: () => {
      onSuccess && onSuccess();
      removeAccessToken();
      navigate(PATH.MAIN);
      location.reload();
    },
  });

  return { mutate };
}

function usePatchProfile({ accessToken }: usePatchProfileProps) {
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
