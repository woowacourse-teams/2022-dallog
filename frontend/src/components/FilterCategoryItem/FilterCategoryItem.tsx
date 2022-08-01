import useToggle from '@/hooks/useToggle';

import { SubscriptionType } from '@/@types/subscription';

import Button from '@/components/@common/Button/Button';

import { PALETTE } from '@/constants';

import { BiPalette } from 'react-icons/bi';
import { RiCheckboxFill } from 'react-icons/ri';

import {
  checkBoxNameStyle,
  colorStyle,
  iconStyle,
  itemStyle,
  nameStyle,
  outerStyle,
  paletteLayoutStyle,
  paletteStyle,
} from './FilterCategoryItem.styles';

interface FilterItemProps {
  subscription: SubscriptionType;
}

function FilterCategoryItem({ subscription }: FilterItemProps) {
  const { state: isPaletteOpen, toggleState: togglePaletteOpen } = useToggle();

  return (
    <div css={itemStyle}>
      <div css={checkBoxNameStyle}>
        <RiCheckboxFill size={20} color={subscription.color} />
        <span css={nameStyle}>{subscription.category.name}</span>
      </div>
      <div css={paletteLayoutStyle}>
        <Button cssProp={iconStyle} onClick={togglePaletteOpen}>
          <BiPalette size={20} />
        </Button>
        {isPaletteOpen && (
          <>
            <div css={outerStyle} onClick={togglePaletteOpen} />
            <div css={paletteStyle}>
              {PALETTE.map((color) => {
                return <Button key={color} cssProp={colorStyle(color)}></Button>;
              })}
            </div>
          </>
        )}
      </div>
    </div>
  );
}

export default FilterCategoryItem;
