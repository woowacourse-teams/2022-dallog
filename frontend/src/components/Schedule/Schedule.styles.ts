import { css, Theme } from '@emotion/react';

import { SCHEDULE } from '@/constants/style';

const itemWithBackgroundStyle = (
  { colors }: Theme,
  priority: number | null,
  maxScheduleCount: number,
  isEndDate: boolean,
  isHovering: boolean,
  colorCode: string
) => css`
  display: ${priority && priority >= maxScheduleCount ? 'none' : 'block'};
  overflow: hidden;
  position: absolute;
  top: ${priority && priority * SCHEDULE.HEIGHT_WITH_MARGIN + 1}rem;

  width: ${isEndDate ? '96%' : '100%'};
  height: ${SCHEDULE.HEIGHT}rem;
  padding: 1rem;
  ${isEndDate &&
  css`
    border-top-right-radius: 4px;
    border-bottom-right-radius: 4px;
  `}

  background: ${colorCode === '' ? colors.ORANGE_500 : colorCode};

  font-size: 2.75rem;
  color: white;
  white-space: nowrap;
  text-overflow: ellipsis;
  line-height: 2.75rem;

  cursor: pointer;
  filter: ${isHovering && 'brightness(0.95)'};

  transition: background-color 0.3s;
`;

const itemWithoutBackgroundStyle = (
  theme: Theme,
  priority: number | null,
  maxScheduleCount: number,
  isEndDate: boolean,
  isHovering: boolean,
  colorCode: string
) => css`
  ${itemWithBackgroundStyle(theme, priority, maxScheduleCount, isEndDate, isHovering, colorCode)};

  border-left: 3px solid ${colorCode === '' ? theme.colors.ORANGE_500 : colorCode};

  background: ${isHovering ? theme.colors.GRAY_000 : theme.colors.WHITE};

  color: black;

  filter: none;

  transition: background-color 0.3s;
`;

export { itemWithBackgroundStyle, itemWithoutBackgroundStyle };
