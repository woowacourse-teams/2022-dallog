import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { CategoryType } from '@/@types/category';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import CategoryModifyModal from '@/components/CategoryModifyModal/CategoryModifyModal';

import { CACHE_KEY } from '@/constants';
import { CATEGORY_TYPE } from '@/constants/category';
import { CONFIRM_MESSAGE, TOOLTIP_MESSAGE } from '@/constants/message';

import { getISODateString } from '@/utils/date';

import categoryApi from '@/api/category';

import { FiEdit3 } from 'react-icons/fi';
import { RiDeleteBin6Line } from 'react-icons/ri';

import {
  buttonStyle,
  categoryItemStyle,
  grayTextStyle,
  itemStyle,
  menuTitle,
} from './MyCategoryItem.style';

interface MyCategoryItemProps {
  category: CategoryType;
}

function MyCategoryItem({ category }: MyCategoryItemProps) {
  const { accessToken } = useRecoilValue(userState);

  const { state: isCategoryModifyModalOpen, toggleState: toggleCategoryModifyModalOpen } =
    useToggle();

  const queryClient = useQueryClient();
  const { mutate } = useMutation(() => categoryApi.delete(accessToken, category.id), {
    onSuccess: () => onSuccessDeleteCategory(),
  });

  const handleClickDeleteButton = () => {
    if (confirm(CONFIRM_MESSAGE.DELETE)) {
      mutate();
    }
  };

  const onSuccessDeleteCategory = () => {
    queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
  };

  const canEditCategory = category.categoryType !== CATEGORY_TYPE.PERSONAL;

  return (
    <div css={categoryItemStyle}>
      <span css={itemStyle}>{getISODateString(category.createdAt)}</span>
      <span css={itemStyle}>
        {category.name}
        <span css={grayTextStyle}>
          {category.categoryType === CATEGORY_TYPE.GOOGLE && ' (구글)'}
          {category.categoryType === CATEGORY_TYPE.PERSONAL && ' (기본)'}
        </span>
      </span>
      <div css={itemStyle}>
        <ModalPortal isOpen={isCategoryModifyModalOpen} closeModal={toggleCategoryModifyModalOpen}>
          <CategoryModifyModal category={category} closeModal={toggleCategoryModifyModalOpen} />
        </ModalPortal>
        <Button
          cssProp={buttonStyle}
          onClick={toggleCategoryModifyModalOpen}
          disabled={!canEditCategory}
        >
          <FiEdit3 size={20} />
          {!canEditCategory ? (
            <span css={menuTitle}>{TOOLTIP_MESSAGE.CANNOT_EDIT_DELETE_DEFAULT_CATEGORY}</span>
          ) : (
            <></>
          )}
        </Button>
        <Button cssProp={buttonStyle} onClick={handleClickDeleteButton} disabled={!canEditCategory}>
          <RiDeleteBin6Line size={20} />
          {!canEditCategory ? (
            <span css={menuTitle}>{TOOLTIP_MESSAGE.CANNOT_EDIT_DELETE_DEFAULT_CATEGORY}</span>
          ) : (
            <></>
          )}
        </Button>
      </div>
    </div>
  );
}

export default MyCategoryItem;
