import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useValidateCategory from '@/hooks/useValidateCategory';

import { CategoryType } from '@/@types/category';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CACHE_KEY } from '@/constants/api';
import { CATEGORY_TYPE } from '@/constants/category';

import categoryApi from '@/api/category';

import {
  cancelButtonStyle,
  categoryAddModal,
  content,
  controlButtons,
  form,
  saveButtonStyle,
  title,
} from './CategoryAddModal.styles';

interface CategoryAddModalProps {
  closeModal: () => void;
}

function CategoryAddModal({ closeModal }: CategoryAddModalProps) {
  const { accessToken } = useRecoilValue(userState);

  const theme = useTheme();

  const { categoryValue, getCategoryErrorMessage, isValidCategory } = useValidateCategory();

  const queryClient = useQueryClient();
  const { mutate } = useMutation<
    AxiosResponse<CategoryType>,
    AxiosError,
    Pick<CategoryType, 'name' | 'categoryType'>,
    unknown
  >((body) => categoryApi.post(accessToken, body), {
    onSuccess: () => onSuccessPostCategory(),
  });

  const handleSubmitCategoryAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    mutate({ name: categoryValue.inputValue, categoryType: CATEGORY_TYPE.NORMAL });
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
            value={categoryValue.inputValue}
            autoFocus={true}
            onChange={categoryValue.onChangeValue}
            isValid={isValidCategory}
            errorMessage={getCategoryErrorMessage()}
            labelText="카테고리 이름"
          />
        </div>
        <div css={controlButtons}>
          <Button cssProp={cancelButtonStyle(theme)} onClick={closeModal}>
            취소
          </Button>
          <Button type="submit" cssProp={saveButtonStyle(theme)} disabled={!isValidCategory}>
            완료
          </Button>
        </div>
      </form>
    </div>
  );
}

export default CategoryAddModal;
