import { css, Theme } from '@emotion/react';

const scheduleModifyModal = ({ colors }: Theme) => css`
  width: 120rem;
  height: 160rem;
  padding: 12.5rem;
  border-radius: 12px;

  background: ${colors.WHITE};
`;

const form = ({ flex }: Theme) => css`
  ${flex.column};

  justify-content: space-between;

  height: 100%;
`;

const categoryStyle = ({ colors }: Theme, colorCode: string) => css`
  padding: 0 3rem;

  width: 100%;
  height: 12rem;
  border: 1px solid ${colors.GRAY_500};
  border-radius: 8px;

  background: ${colorCode};

  font-size: 5rem;
  color: ${colors.WHITE};
  line-height: 12rem;

  &:hover {
    cursor: default;
  }
`;

const allDayButton = ({ colors }: Theme, isAllDay: boolean) => css`
  width: 100%;
  height: 9rem;
  border: 1px solid ${colors.GRAY_500};
  border-radius: 8px;
  filter: drop-shadow(0 2px 2px ${colors.GRAY_400});

  background: ${isAllDay ? colors.YELLOW_500 : colors.WHITE};

  font-size: 5rem;
  color: ${isAllDay ? colors.WHITE : colors.GRAY_600};
`;

const dateTime = ({ flex }: Theme) => css`
  ${flex.column}

  gap: 2.5rem;

  width: 100%;
`;

const arrow = ({ colors }: Theme) => css`
  font-size: 6rem;
  font-weight: bold;
  color: ${colors.GRAY_500};
`;

const controlButtons = ({ flex }: Theme) => css`
  ${flex.row}

  align-self: flex-end;
  gap: 5rem;
`;

const cancelButton = ({ colors }: Theme) => css`
  width: 22.5rem;
  height: 10rem;
  border: 2px solid ${colors.GRAY_500};
  border-radius: 8px;
  filter: drop-shadow(0 2px 2px ${colors.GRAY_400});

  background: ${colors.WHITE};

  font-size: 5rem;
  color: ${colors.GRAY_600};
`;

const saveButton = ({ colors }: Theme) => css`
  width: 22.5rem;
  height: 10rem;
  border-radius: 8px;
  filter: drop-shadow(0px 2px 2px ${colors.GRAY_400});

  background: ${colors.YELLOW_500};

  font-size: 5rem;
  color: ${colors.WHITE};
`;

export {
  allDayButton,
  arrow,
  cancelButton,
  categoryStyle,
  controlButtons,
  dateTime,
  form,
  saveButton,
  scheduleModifyModal,
};
