import { css, Theme } from '@emotion/react';

const categoryPageStyle = css`
  height: 80%;
  padding: 9rem;
`;

const searchFormStyle = css`
  position: relative;

  width: 100%;
  height: 12rem;
  margin-bottom: 8rem;
`;

const searchButtonStyle = css`
  position: absolute;

  top: 50%;
  transform: translateY(-50%);

  width: 12rem;
`;

const searchFieldsetStyle = css`
  height: 100%;
`;

const searchInputStyle = css`
  height: 100%;
  padding-left: 12rem;

  font-size: 4rem;
`;

const buttonStyle = ({ colors }: Theme) => css`
  width: 40rem;
  height: 12rem;
  border-radius: 8px;
  border: 1px solid ${colors.GRAY_500};

  background: ${colors.YELLOW_500};

  font-size: 3.5rem;
  font-weight: 700;
  line-height: 3.5rem;
  color: ${colors.WHITE};

  &:hover {
    box-shadow: none;
  }
`;

const controlStyle = ({ flex }: Theme) => css`
  ${flex.row};

  align-items: flex-start;
  justify-content: center;
  gap: 4rem;

  width: 100%;
`;

const outLineButtonStyle = ({ colors }: Theme) => css`
  width: 40rem;
  height: 12rem;
  border-radius: 8px;
  border: 1px solid ${colors.GRAY_500};

  font-size: 3.5rem;
  font-weight: 700;
  line-height: 3.5rem;
  color: ${colors.YELLOW_500};
`;

const toggleModeStyle = ({ colors, flex }: Theme, mode: 'ALL' | 'MY') => css`
  ${flex.row};

  justify-content: space-around;

  width: 35rem;
  height: 12rem;
  padding: 0 1rem;
  border-radius: 8px;
  border: 1px solid ${colors.GRAY_500};

  background: linear-gradient(
    90deg,
    ${mode === 'ALL' ? colors.YELLOW_500 : colors.WHITE} 50%,
    ${mode === 'MY' ? colors.YELLOW_500 : colors.WHITE} 50%
  );
`;

const modeTextStyle = ({ colors }: Theme, isSelected: boolean) => css`
  font-size: 3.5rem;
  font-weight: 700;
  line-height: 3.5rem;
  color: ${isSelected ? colors.WHITE : colors.YELLOW_500};
`;

export {
  buttonStyle,
  categoryPageStyle,
  controlStyle,
  modeTextStyle,
  outLineButtonStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
  toggleModeStyle,
};
