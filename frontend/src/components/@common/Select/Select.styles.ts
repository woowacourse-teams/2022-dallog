import { css, Theme } from '@emotion/react';

import { OPTION_HEIGHT } from '@/constants/style';

const layoutStyle = css`
  width: 100%;
`;

const hiddenStyle = css`
  display: none;
`;

const dimmerStyle = (isSelectOpen: boolean) => css`
  ${!isSelectOpen && hiddenStyle};

  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background: transparent;
`;

const selectStyle = ({ colors }: Theme) => css`
  width: 100%;
  height: 11.75rem;
  border-radius: 8px;
  border: 1px solid ${colors.GRAY_400};

  font-size: 4rem;
  text-align: center;
  line-height: 12rem;

  cursor: pointer;

  &:focus {
    outline: none;
    border-color: ${colors.YELLOW_500};
    box-shadow: 0 0 2px ${colors.YELLOW_500};
  }
`;

const optionLayoutStyle = ({ colors }: Theme, isSelectOpen: boolean) => css`
  position: absolute;
  overflow: overlay;

  width: 100%;
  max-height: ${isSelectOpen ? '50rem' : 0};
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
  z-index: 30;
`;

export {
  dimmerStyle,
  labelStyle,
  layoutStyle,
  hiddenStyle,
  selectStyle,
  optionStyle,
  optionLayoutStyle,
  relativeStyle,
};
