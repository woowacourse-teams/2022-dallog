import { validateLength } from '@/validation';
import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useControlledInput from '@/hooks/useControlledInput';

import { CategoryType } from '@/@types/category';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CACHE_KEY } from '@/constants';
import { VALIDATION_MESSAGE, VALIDATION_SIZE } from '@/constants/validate';

import categoryApi from '@/api/category';

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
  const { accessToken } = useRecoilValue(userState);

  const theme = useTheme();

  const { inputValue, onChangeValue } = useControlledInput();

  const queryClient = useQueryClient();
  const { mutate } = useMutation<
    AxiosResponse<CategoryType>,
    AxiosError,
    Pick<CategoryType, 'name'>,
    unknown
  >((body) => categoryApi.post(accessToken, body), {
    onSuccess: () => onSuccessPostCategory(),
  });

  const handleSubmitCategoryAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    mutate({ name: inputValue });
  };

  const onSuccessPostCategory = () => {
    queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);

    closeModal();
  };

  return (
    <div css={categoryAddModal}>
      <h1 css={title}>새 카테고리 만들기</h1>
      <form css={form} onSubmit={handleSubmitCategoryAddForm}>
        <div css={content}>
          <Fieldset
            placeholder="이름"
            value={inputValue}
            autoFocus={true}
            onChange={onChangeValue}
            isValid={validateLength(
              inputValue,
              VALIDATION_SIZE.MIN_LENGTH,
              VALIDATION_SIZE.CATEGORY_NAME_MAX_LENGTH
            )}
            errorMessage={VALIDATION_MESSAGE.STRING_LENGTH(
              VALIDATION_SIZE.MIN_LENGTH,
              VALIDATION_SIZE.CATEGORY_NAME_MAX_LENGTH
            )}
          />
        </div>
        <div css={controlButtons}>
          <Button cssProp={cancelButton(theme)} onClick={closeModal}>
            취소
          </Button>
          <Button
            type="submit"
            cssProp={saveButton(theme)}
            disabled={
              !validateLength(
                inputValue,
                VALIDATION_SIZE.MIN_LENGTH,
                VALIDATION_SIZE.CATEGORY_NAME_MAX_LENGTH
              )
            }
          >
            완료
          </Button>
        </div>
      </form>
    </div>
  );
}

export default CategoryAddModal;
