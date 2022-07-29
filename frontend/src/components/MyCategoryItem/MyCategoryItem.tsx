import { CategoryType } from '@/@types/category';

import Button from '@/components/@common/Button/Button';

import { FiEdit3 } from 'react-icons/fi';
import { RiDeleteBin6Line } from 'react-icons/ri';

import { buttonStyle, controlButtonsStyle, itemStyle, nameStyle } from './MyCategoryItem.style';

interface MyCategoryItemProps {
  category: CategoryType;
}

function MyCategoryItem({ category }: MyCategoryItemProps) {
  return (
    <div css={itemStyle}>
      <span css={nameStyle}>{category.name}</span>
      <div css={controlButtonsStyle}>
        <Button cssProp={buttonStyle}>
          <FiEdit3 size={20} />
        </Button>
        <Button cssProp={buttonStyle}>
          <RiDeleteBin6Line size={20} />
        </Button>
      </div>
    </div>
  );
}

export default MyCategoryItem;
