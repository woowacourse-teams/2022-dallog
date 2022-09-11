import { css, Theme } from '@emotion/react';

import { ModalPosType } from '@/@types';

const itemStyle = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
  height: 8rem;

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

  &:hover {
    cursor: pointer;
  }
`;

const nameStyle = css`
  overflow: hidden;
  position: relative;

  width: 32rem;

  white-space: nowrap;
  text-overflow: ellipsis;
`;

const colorStyle = (color: string) => css`
  width: 5rem;
  height: 5rem;
  border-radius: 50%;

  background: ${color};

  &:hover {
    filter: none;
    transform: scale(1.2);
  }
`;

const headerStyle = css`
  padding: 2rem;

  font-size: 5rem;
`;

const iconStyle = css`
  visibility: hidden;
`;

const paletteStyle = ({ colors }: Theme, modalPos: ModalPosType) => css`
  position: absolute;
  top: ${modalPos.top ? `${modalPos.top + 5}px` : 'none'};
  right: ${modalPos.right ? `${modalPos.right + 5}px` : 'none'};
  bottom: ${modalPos.bottom ? `${modalPos.bottom + 5}px` : 'none'};
  left: ${modalPos.left ? `${modalPos.left + 5}px` : 'none'};
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
  z-index: 20;

  width: 100%;
  height: 100%;

  background-color: transparent;
`;

export {
  itemStyle,
  colorStyle,
  checkBoxNameStyle,
  headerStyle,
  iconStyle,
  nameStyle,
  outerStyle,
  paletteStyle,
  paletteLayoutStyle,
};
