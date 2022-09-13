import { css, Theme } from '@emotion/react';

import { ModalPosType } from '@/@types';

const controlButtonStyle = ({ colors, flex }: Theme) => css`
  ${flex.row};

  justify-content: flex-start;
  gap: 1rem;

  width: 100%;
  padding: 2rem;
  border-bottom: 1px solid ${colors.GRAY_300};
  box-sizing: contain;

  &:hover {
    filter: none;
    background-color: ${colors.GRAY_100};
  }
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

const modalPosStyle = ({ colors, flex }: Theme, modalPos: ModalPosType) => css`
  ${flex.column};

  align-items: flex-start;
  position: absolute;
  top: ${modalPos.top ? `${modalPos.top}px` : 'none'};
  right: ${modalPos.right ? `${modalPos.right}px` : 'none'};
  bottom: ${modalPos.bottom ? `${modalPos.bottom}px` : 'none'};
  left: ${modalPos.left ? `${modalPos.left}px` : 'none'};

  border: 1px solid ${colors.GRAY_300};
  border-radius: 4px;

  background: ${colors.WHITE};
`;

const paletteStyle = css`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  place-items: center;
  gap: 2rem;

  width: 35rem;
  padding: 2rem;
`;

const outerStyle = css`
  position: fixed;
  left: 0;
  top: 16rem;

  width: 100%;
  height: 100%;

  background-color: transparent;
`;

export { colorStyle, controlButtonStyle, modalPosStyle, outerStyle, paletteStyle };
