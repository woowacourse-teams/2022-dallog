import { css, Theme } from '@emotion/react';

const categoryItemStyle = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 64rem;
  padding: 2rem 3rem;

  &:hover {
    background-color: ${colors.GRAY_100};

    button {
      visibility: visible;
    }
  }
`;

const checkBoxNameStyle = ({ flex }: Theme) => css`
  ${flex.row}

  gap: 1rem;
`;

const categoryNameStyle = css`
  font-size: 3.5rem;
`;

const colorStyle = (color: string) => css`
  width: 5rem;
  height: 5rem;
  border-radius: 50%;

  background: ${color};
`;

const headerStyle = css`
  padding: 2rem;

  font-size: 5rem;
`;

const iconStyle = css`
  visibility: hidden;
`;

const paletteStyle = ({ colors }: Theme) => css`
  position: absolute;
  z-index: 30;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  place-items: center;
  gap: 2rem;

  width: 35rem;
  padding: 2rem;
  border: 1px solid ${colors.GRAY_300};
  border-radius: 4px;

  background: ${colors.WHITE};
`;

const paletteLayoutStyle = css`
  position: relative;
`;

const outerStyle = css`
  position: fixed;
  left: 0;
  top: 16rem;

  width: 100%;
  height: 100%;

  background-color: transparent;
`;

export {
  categoryItemStyle,
  colorStyle,
  checkBoxNameStyle,
  categoryNameStyle,
  headerStyle,
  outerStyle,
  iconStyle,
  paletteStyle,
  paletteLayoutStyle,
};
