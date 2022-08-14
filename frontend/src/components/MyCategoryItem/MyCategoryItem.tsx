import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { CategoryType } from '@/@types/category';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import CategoryModifyModal from '@/components/CategoryModifyModal/CategoryModifyModal';

import { CACHE_KEY } from '@/constants';
import { CONFIRM_MESSAGE } from '@/constants/message';

import { getISODateString } from '@/utils/date';

import categoryApi from '@/api/category';

import { FiEdit3 } from 'react-icons/fi';
import { RiDeleteBin6Line } from 'react-icons/ri';

import { buttonStyle, categoryItemStyle, itemStyle } from './MyCategoryItem.style';

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
    useErrorBoundary: true,
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

  return (
    <div css={categoryItemStyle}>
      <span css={itemStyle}>{getISODateString(category.createdAt)}</span>
      <span css={itemStyle}>{category.name}</span>
      <div css={itemStyle}>
        <ModalPortal isOpen={isCategoryModifyModalOpen} closeModal={toggleCategoryModifyModalOpen}>
          <CategoryModifyModal category={category} closeModal={toggleCategoryModifyModalOpen} />
        </ModalPortal>
        <Button cssProp={buttonStyle} onClick={toggleCategoryModifyModalOpen}>
          <FiEdit3 size={20} />
        </Button>
        <Button cssProp={buttonStyle} onClick={handleClickDeleteButton}>
          <RiDeleteBin6Line size={20} />
        </Button>
      </div>
    </div>
  );
}

export default MyCategoryItem;
