import useToggle from '@/hooks/useToggle';

import { CategoryType } from '@/@types/category';

import Button from '@/components/@common/Button/Button';

import { PALETTE } from '@/constants';

import { BiPalette } from 'react-icons/bi';
import { RiCheckboxFill } from 'react-icons/ri';

import {
  categoryItemStyle,
  categoryNameStyle,
  checkBoxNameStyle,
  colorStyle,
  iconStyle,
  outerStyle,
  paletteLayoutStyle,
  paletteStyle,
} from './FilterCategoryItem.styles';

interface FilterItemProps {
  category: CategoryType;
}

function FilterCategoryItem({ category }: FilterItemProps) {
  const { state, toggleState } = useToggle();

  return (
    <div css={categoryItemStyle} key={category.id}>
      <div css={checkBoxNameStyle}>
        <RiCheckboxFill size="5rem" />
        <span css={categoryNameStyle}>{category.name}</span>
      </div>
      <div css={paletteLayoutStyle}>
        <Button cssProp={iconStyle} onClick={toggleState}>
          <BiPalette size="3.5rem" />
        </Button>
        {state && (
          <>
            <div css={outerStyle} onClick={toggleState} />
            <div css={paletteStyle}>
              {PALETTE.map((color) => {
                return <div key={color} css={colorStyle(color)}></div>;
              })}
            </div>
          </>
        )}
      </div>
    </div>
  );
}

export default FilterCategoryItem;
