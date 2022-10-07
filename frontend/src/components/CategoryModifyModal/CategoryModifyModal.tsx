import { useTheme } from '@emotion/react';

import { usePatchCategoryName } from '@/hooks/@queries/category';
import useValidateCategory from '@/hooks/useValidateCategory';

import { CategoryType } from '@/@types/category';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

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
  const theme = useTheme();

  const { categoryValue, getCategoryErrorMessage, isValidCategory } = useValidateCategory(
    category.name
  );

  const { mutate } = usePatchCategoryName({ categoryId: category.id, onSuccess: closeModl });

  const handleSubmitCategoryModifyForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    mutate({ name: categoryValue.inputValue });
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
            labelText="카테고리 이름"
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
