import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY, CONFIRM_MESSAGE } from '@/constants';

import categoryApi from '@/api/category';

import { FiEdit3 } from 'react-icons/fi';
import { RiDeleteBin6Line } from 'react-icons/ri';

import { buttonStyle, controlButtonsStyle, itemStyle, nameStyle } from './MyCategoryItem.style';

interface MyCategoryItemProps {
  category: CategoryType;
}

function MyCategoryItem({ category }: MyCategoryItemProps) {
  const { accessToken } = useRecoilValue(userState);

  const queryClient = useQueryClient();
  const { mutate } = useMutation(() => categoryApi.delete(accessToken, category.id), {
    onSuccess: () => onSuccessDeleteCategory(),
  });

  const handleClickDeleteButton = () => {
    if (confirm(CONFIRM_MESSAGE.DELETE_CATEGORY)) {
      mutate();
    }
  };

  const onSuccessDeleteCategory = () => {
    queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
  };

  return (
    <div css={itemStyle}>
      <span css={nameStyle}>{category.name}</span>
      <div css={controlButtonsStyle}>
        <Button cssProp={buttonStyle}>
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
