import { css, Theme } from '@emotion/react';

import { OPTION_HEIGHT } from '@/constants/style';

const hiddenStyle = css`
  display: none;
`;

const selectStyle = ({ colors }: Theme) => css`
  width: 42.75rem;
  height: 11.75rem;
  border-radius: 8px;
  border: 1px solid ${colors.GRAY_400};
  box-sizing: contain;

  font-size: 4rem;
  text-align: center;
  line-height: 12rem;

  &:focus {
    outline: none;
    border-color: ${colors.YELLOW_500};
    box-shadow: 0 0 2px ${colors.YELLOW_500};
  }
`;

const optionLayoutStyle = ({ colors }: Theme, isSelectOpen: boolean) => css`
  position: absolute;
  overflow: overlay;

  width: 42.75rem;
  height: ${isSelectOpen ? '50rem' : 0};
  border: ${isSelectOpen && `1px solid ${colors.GRAY_400}`};
  border-radius: 8px;

  background: ${colors.WHITE};

  &:focus {
    outline: none;
    border-color: ${colors.YELLOW_500};
    box-shadow: 0 0 2px ${colors.YELLOW_500};
  }
`;

const optionStyle = ({ colors }: Theme, isSelected: boolean) => css`
  height: ${OPTION_HEIGHT};

  background: ${isSelected && colors.GRAY_200};

  font-size: 4rem;

  &:hover {
    background: ${!isSelected && colors.GRAY_100};
  }
`;

const labelStyle = css`
  display: block;

  padding: 2.5rem 0;

  text-align: center;
`;

const relativeStyle = css`
  position: relative;
`;

export { labelStyle, hiddenStyle, selectStyle, optionStyle, optionLayoutStyle, relativeStyle };
