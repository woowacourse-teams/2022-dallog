import { useTheme } from '@emotion/react';

import { usePostCategory } from '@/hooks/@queries/category';
import useValidateCategory from '@/hooks/useValidateCategory';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CATEGORY_TYPE } from '@/constants/category';

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
  const theme = useTheme();

  const { categoryValue, getCategoryErrorMessage, isValidCategory } = useValidateCategory();

  const { mutate } = usePostCategory({ onSuccess: closeModal });

  const handleSubmitCategoryAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    mutate({ name: categoryValue.inputValue, categoryType: CATEGORY_TYPE.NORMAL });
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
