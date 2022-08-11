import { validateLength } from '@/validation';
import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useState } from 'react';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useControlledInput from '@/hooks/useControlledInput';

import { CategoryType } from '@/@types/category';
import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CACHE_KEY, PALETTE, VALIDATION_MESSAGE } from '@/constants';

import { getRandomNumber } from '@/utils';

import categoryApi from '@/api/category';
import subscriptionApi from '@/api/subscription';

import {
  cancelButton,
  categoryAddModal,
  content,
  controlButtons,
  form,
  saveButton,
  title,
} from './CategoryAddModal.styles';

interface CategoryAddModalProps {
  closeModal: () => void;
}

function CategoryAddModal({ closeModal }: CategoryAddModalProps) {
  const [myCategoryId, setMyCategoryId] = useState(0);
  const theme = useTheme();

  const { inputValue, onChange } = useControlledInput();

  const { accessToken } = useRecoilValue(userState);

  const subscriptionPostBody = {
    colorCode: PALETTE[getRandomNumber(0, PALETTE.length)],
  };

  const queryClient = useQueryClient();
  const { mutate: postCategory } = useMutation<
    AxiosResponse<CategoryType>,
    AxiosError,
    Pick<CategoryType, 'name'>,
    unknown
  >((body) => categoryApi.post(accessToken, body), {
    onSuccess: (data) => onSuccessPostCategory(data),
  });

  const { mutate: postSubscription } = useMutation<
    AxiosResponse<Pick<SubscriptionType, 'colorCode'>>,
    AxiosError,
    Pick<SubscriptionType, 'colorCode'>,
    unknown
  >(() => subscriptionApi.post(accessToken, myCategoryId, subscriptionPostBody), {
    onSuccess: () => onSuccessPostSubscription(),
  });

  const handleSubmitCategoryAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    postCategory({ name: inputValue });
  };

  const onSuccessPostCategory = ({ data }: AxiosResponse<CategoryType>) => {
    queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);

    setMyCategoryId(data.id);
    postSubscription(subscriptionPostBody);

    closeModal();
  };

  const onSuccessPostSubscription = () => {
    queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
  };

  return (
    <div css={categoryAddModal}>
      <h1 css={title}>새 카테고리 만들기</h1>
      <form css={form} onSubmit={handleSubmitCategoryAddForm}>
        <div css={content}>
          <Fieldset
            placeholder="이름"
            autoFocus={true}
            onChange={onChange}
            errorMessage={VALIDATION_MESSAGE.ONE_TO_TWENTY_LENGTH}
            isValid={validateLength(inputValue, 1, 20)}
          />
        </div>
        <div css={controlButtons}>
          <Button cssProp={cancelButton(theme)} onClick={closeModal}>
            취소
          </Button>
          <Button
            type="submit"
            cssProp={saveButton(theme)}
            disabled={!validateLength(inputValue, 1, 20)}
          >
            완료
          </Button>
        </div>
      </form>
    </div>
  );
}

export default CategoryAddModal;
