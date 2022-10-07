import { useDeleteCategory, usePatchCategoryName } from '@/hooks/@queries/category';
import useValidateCategory from '@/hooks/useValidateCategory';

import { SubscriptionType } from '@/@types/subscription';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CONFIRM_MESSAGE } from '@/constants/message';

import { MdClose } from 'react-icons/md';

import {
  closeModalButtonStyle,
  deleteButtonStyle,
  headerStyle,
  layoutStyle,
  renameButtonStyle,
  renameFieldSetStyle,
  renameFormStyle,
  sectionStyle,
  spaceBetweenStyle,
  titleStyle,
} from './GoogleCategoryManageModal.styles';

interface GoogleCategoryManageModalProps {
  subscription: SubscriptionType;
  closeModal: () => void;
}

function GoogleCategoryManageModal({ subscription, closeModal }: GoogleCategoryManageModalProps) {
  const { categoryValue, getCategoryErrorMessage, isValidCategory } = useValidateCategory(
    subscription.category.name
  );

  const { mutate: patchCategory } = usePatchCategoryName({
    categoryId: subscription.category.id,
    onSuccess: closeModal,
  });

  const { mutate: deleteCategory } = useDeleteCategory({
    categoryId: subscription.category.id,
    onSuccess: closeModal,
  });

  const handleSubmitCategoryModifyForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    patchCategory({ name: categoryValue.inputValue });
  };

  const handleClickDeleteCategoryButton = () => {
    window.confirm(CONFIRM_MESSAGE.DELETE) && deleteCategory();
  };

  return (
    <div css={layoutStyle}>
      <h1 css={headerStyle}>{subscription.category.name} (관리)</h1>
      <Button cssProp={closeModalButtonStyle} onClick={closeModal}>
        <MdClose />
      </Button>
      <section css={sectionStyle}>
        <h2 css={titleStyle}>카테고리 이름 수정</h2>
        <form css={renameFormStyle} onSubmit={handleSubmitCategoryModifyForm}>
          <div css={spaceBetweenStyle}>
            <Fieldset
              placeholder={subscription.category.name}
              value={categoryValue.inputValue}
              autoFocus
              onChange={categoryValue.onChangeValue}
              isValid={isValidCategory}
              errorMessage={getCategoryErrorMessage()}
              cssProp={renameFieldSetStyle}
            />
            <Button type="submit" disabled={!isValidCategory} cssProp={renameButtonStyle}>
              수정
            </Button>
          </div>
        </form>
      </section>

      <section css={sectionStyle}>
        <h2 css={titleStyle}>카테고리 삭제</h2>
        <div css={spaceBetweenStyle}>
          <span>카테고리를 영구적으로 삭제합니다.</span>
          <Button onClick={handleClickDeleteCategoryButton} cssProp={deleteButtonStyle}>
            삭제
          </Button>
        </div>
      </section>
    </div>
  );
}

export default GoogleCategoryManageModal;
