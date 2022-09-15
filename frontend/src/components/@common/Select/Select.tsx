import { useTheme } from '@emotion/react';
import { useEffect, useRef } from 'react';

import useToggle from '@/hooks/useToggle';

import { OPTION_HEIGHT } from '@/constants/style';

import {
  hiddenStyle,
  labelStyle,
  optionLayoutStyle,
  optionStyle,
  relativeStyle,
  selectStyle,
} from './Select.styles';

interface SelectProps {
  options: string[];
  value: string;
  onChange: ({
    target,
  }: React.ChangeEvent<HTMLInputElement> | React.ChangeEvent<HTMLSelectElement>) => void;
}

function Select({ options, value, onChange }: SelectProps) {
  const theme = useTheme();

  const ref = useRef<HTMLDivElement>(null);

  const { state: isSelectOpen, toggleState: toggleSelectOpen } = useToggle(false);

  const selectedPosition = options.findIndex((opt) => opt === value);

  useEffect(() => {
    ref.current?.scrollTo(0, selectedPosition * OPTION_HEIGHT);
  });

  return (
    <div>
      <div css={selectStyle} onClick={toggleSelectOpen}>
        {value || '옵션 선택'}
      </div>
      <div css={relativeStyle}>
        <div css={optionLayoutStyle(theme, isSelectOpen)} ref={ref}>
          {isSelectOpen &&
            options.map((opt, index) => (
              <div key={index} css={optionStyle(theme, opt === value)}>
                <input
                  type="radio"
                  id={opt}
                  value={opt}
                  onChange={onChange}
                  name="option-picker"
                  css={hiddenStyle}
                  onClick={toggleSelectOpen}
                />
                <label htmlFor={opt} css={labelStyle}>
                  {opt}
                </label>
              </div>
            ))}
        </div>
      </div>
    </div>
  );
}

export default Select;
