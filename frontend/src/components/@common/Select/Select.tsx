import { useTheme } from '@emotion/react';
import { useEffect, useRef } from 'react';

import useToggle from '@/hooks/useToggle';

import { SelectCssPropType } from '@/@types';

import { OPTION_HEIGHT } from '@/constants/style';

import {
  dimmerStyle,
  hiddenStyle,
  labelStyle,
  layoutStyle,
  optionLayoutStyle,
  optionStyle,
  relativeStyle,
  selectStyle,
} from './Select.styles';

type OptionsType = { id: number | string; name: number | string };

interface SelectProps {
  options: Array<OptionsType>;
  value: string;
  onChange: ({
    target,
  }: React.ChangeEvent<HTMLInputElement> | React.ChangeEvent<HTMLSelectElement>) => void;
  cssProp?: SelectCssPropType;
  description?: string;
}

function Select({ options, value, onChange, cssProp, description = '옵션 선택' }: SelectProps) {
  const theme = useTheme();

  const ref = useRef<HTMLDivElement>(null);

  const { state: isSelectOpen, toggleState: toggleSelectOpen } = useToggle(false);

  const selectedPosition = options.findIndex((opt) => String(opt.id) === value);

  useEffect(() => {
    ref.current?.scrollTo(0, selectedPosition * OPTION_HEIGHT);
  });

  const handleClickDimmer = (e: React.MouseEvent) => {
    e.stopPropagation();
    toggleSelectOpen();
  };

  return (
    <div css={[layoutStyle, cssProp?.select]}>
      <div css={dimmerStyle(isSelectOpen)} onClick={handleClickDimmer}></div>
      <div css={selectStyle} onClick={toggleSelectOpen}>
        {options.find((opt) => String(opt.id) === value)?.name || description}
      </div>
      <div css={relativeStyle}>
        <div css={[optionLayoutStyle(theme, isSelectOpen), cssProp?.optionBox]} ref={ref}>
          {isSelectOpen &&
            options.map((opt, index) => (
              <div key={index} css={optionStyle(theme, String(opt.id) === value)}>
                <input
                  type="radio"
                  id={`${opt.name}#${opt.id}`}
                  value={opt.id}
                  onChange={onChange}
                  name="option-picker"
                  css={hiddenStyle}
                  onClick={toggleSelectOpen}
                />
                <label htmlFor={`${opt.name}#${opt.id}`} css={[labelStyle, cssProp?.option]}>
                  {opt.name}
                </label>
              </div>
            ))}
        </div>
      </div>
    </div>
  );
}

export default Select;
