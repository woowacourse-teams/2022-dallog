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

  const { categoryValue, getCategoryErrorMessage, isValidCategory } = useValidateCategory(
    category.name
  );

  const queryClient = useQueryClient();
  const { mutate } = useMutation<
    AxiosResponse<Pick<CategoryType, 'name'>>,
    AxiosError,
    Pick<CategoryType, 'name'>,
    unknown
  >((body) => categoryApi.patch(accessToken, category.id, body), {
    onSuccess: () => onSuccessPatchCategory(),
  });

  const handleSubmitCategoryModifyForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    mutate({ name: categoryValue.inputValue });
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
            value={categoryValue.inputValue}
            autoFocus
            onChange={categoryValue.onChangeValue}
            isValid={isValidCategory}
            errorMessage={getCategoryErrorMessage()}
          />
        </div>
        <div css={controlButtons}>
          <Button cssProp={cancelButton(theme)} onClick={closeModal}>
            취소
          </Button>
          <Button type="submit" cssProp={saveButton(theme)} disabled={!isValidCategory}>
            완료
          </Button>
        </div>
      </form>
    </div>
  );
}

export default CategoryModifyModal;
