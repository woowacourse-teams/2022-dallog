import { css, Theme } from '@emotion/react';

import { ModalPosType } from '@/@types';

const dateModalStyle = ({ colors, flex }: Theme, dateModalPos: ModalPosType) => css`
  ${flex.column}

  position: absolute;
  top: ${dateModalPos.top ? `${dateModalPos.top + 20}px` : 'none'};
  right: ${dateModalPos.right ? `${dateModalPos.right + 20}px` : 'none'};
  bottom: ${dateModalPos.bottom ? `${dateModalPos.bottom + 20}px` : 'none'};
  left: ${dateModalPos.left ? `${dateModalPos.left + 20}px` : 'none'};
  gap: 1rem;

  width: 50rem;
  padding: 4rem;
  border-radius: 4px;
  box-shadow: 0 0 5px ${colors.GRAY_500};

  background: ${colors.WHITE};
`;

const headerStyle = ({ flex }: Theme) => css`
  ${flex.column}

  justify-content: flex-end;
  gap: 1rem;

  width: 100%;
  margin-bottom: 2rem;
`;

const dayTextStyle = ({ colors }: Theme, day: number) => css`
  font-size: 3rem;
  color: ${day === 0 && colors.RED_400};
  text-align: right;
`;

const dateTextStyle = ({ colors }: Theme, day: number, isToday: boolean) => css`
  width: 5rem;
  height: 5rem;
  padding: 1rem;
  border-radius: 50%;

  background: ${isToday && colors.YELLOW_500};

  font-size: 2.5rem;
  font-weight: 500;
  color: ${isToday ? colors.WHITE : day === 0 ? colors.RED_400 : colors.GRAY_700};
  text-align: center;
  line-height: 3rem;
`;

const itemWithBackgroundStyle = (colorCode: string) => css`
  width: 100%;
  height: 5rem;
  padding: 1rem;

  background: ${colorCode};

  font-size: 2.75rem;
  color: white;
  white-space: nowrap;
  line-height: 2.75rem;
`;

const itemWithoutBackgroundStyle = ({ colors }: Theme, colorCode: string) => css`
  ${itemWithBackgroundStyle(colorCode)};

  overflow: hidden;

  border-left: 3px solid ${colorCode};

  background: ${colors.WHITE};

  color: black;
`;

export {
  dateModalStyle,
  dateTextStyle,
  dayTextStyle,
  headerStyle,
  itemWithBackgroundStyle,
  itemWithoutBackgroundStyle,
};
