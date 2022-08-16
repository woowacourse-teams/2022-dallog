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

import { CACHE_KEY, VALIDATION_SIZE } from '@/constants';
import { VALIDATION_MESSAGE } from '@/constants/message';

import categoryApi from '@/api/category';

import {
  cancelButton,
  content,
  controlButtons,
  form,
  modal,
  saveButton,
  title,
} from './CategoryModifyModal.styles';

interface CategoryModifyModalProps {
  category: CategoryType;
  closeModal: () => void;
}

function CategoryModifyModal({ category, closeModal }: CategoryModifyModalProps) {
  const { accessToken } = useRecoilValue(userState);

  const theme = useTheme();

  const queryClient = useQueryClient();
  const { mutate } = useMutation<
    AxiosResponse<Pick<CategoryType, 'name'>>,
    AxiosError,
    Pick<CategoryType, 'name'>,
    unknown
  >((body) => categoryApi.patch(accessToken, category.id, body), {
    onSuccess: () => onSuccessPatchCategory(),
  });

  const { inputValue, onChangeValue } = useControlledInput(category.name);

  const handleSubmitCategoryModifyForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    mutate({ name: inputValue });
  };

  const onSuccessPatchCategory = () => {
    queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
    closeModal();
  };

  return (
    <div css={modal}>
      <h1 css={title}>카테고리 이름 수정</h1>
      <form css={form} onSubmit={handleSubmitCategoryModifyForm}>
        <div css={content}>
          <Fieldset
            placeholder={category.name}
            value={inputValue}
            autoFocus
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

export default CategoryModifyModal;
