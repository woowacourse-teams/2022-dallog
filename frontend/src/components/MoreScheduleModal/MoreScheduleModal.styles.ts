import { css, Theme } from '@emotion/react';

import { ModalPosType } from '@/@types';

const moreScheduleModalStyle = (
  { colors, flex, mq }: Theme,
  moreScheduleModalPos: ModalPosType
) => css`
  ${flex.column};

  overflow-x: hidden;
  justify-content: flex-start;
  overflow-y: auto;
  position: absolute;
  top: ${moreScheduleModalPos.top ? `${moreScheduleModalPos.top + 20}px` : 'none'};
  bottom: ${moreScheduleModalPos.bottom ? `${moreScheduleModalPos.bottom + 20}px` : 'none'};
  gap: 1rem;

  width: 50rem;
  max-height: 50%;
  padding: 4rem;
  border-radius: 7px;
  box-shadow: 0 0 5px ${colors.GRAY_500};

  background: ${colors.WHITE};

  ${mq?.laptop} {
    right: ${moreScheduleModalPos.right ? `${moreScheduleModalPos.right + 20}px` : 'none'};
    left: ${moreScheduleModalPos.left ? `${moreScheduleModalPos.left + 20}px` : 'none'};
  }

  ${mq?.tablet || mq?.mobile} {
    left: 50%;
    transform: translateX(-50%);
  }
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

const itemWithBackgroundStyle = ({ colors }: Theme, colorCode: string) => css`
  overflow: hidden;

  width: 100%;
  height: 5rem;
  padding: 1rem;

  background: ${colorCode === '' ? colors.ORANGE_500 : colorCode};

  font-size: 2.75rem;
  color: white;
  white-space: nowrap;
  line-height: 2.75rem;
  text-overflow: ellipsis;

  &:hover {
    cursor: pointer;
    filter: brightness(0.95);
  }
`;

const itemWithoutBackgroundStyle = (theme: Theme, colorCode: string) => css`
  ${itemWithBackgroundStyle(theme, colorCode)};

  overflow: hidden;

  border-left: 3px solid ${colorCode === '' ? theme.colors.ORANGE_500 : colorCode};

  background: ${theme.colors.WHITE};

  color: black;

  &:hover {
    background: ${theme.colors.GRAY_000};
    filter: none;
  }
`;

export {
  moreScheduleModalStyle,
  dateTextStyle,
  dayTextStyle,
  headerStyle,
  itemWithBackgroundStyle,
  itemWithoutBackgroundStyle,
};
