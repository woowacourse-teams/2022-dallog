import { css, Theme } from '@emotion/react';

const scheduleAddModal = ({ colors }: Theme) => css`
  width: 120rem;
  padding: 12.5rem;
  border-radius: 12px;

  background: ${colors.WHITE};
`;

const form = ({ flex }: Theme) => css`
  ${flex.column};

  gap: 6rem;

  height: 100%;
`;

const categorySelect = ({ colors }: Theme) => css`
  width: 100%;
  height: 13rem;
  padding: 3rem;
  border: 1px solid ${colors.GRAY_400};
  border-radius: 8px;

  font-size: 4rem;
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
  categorySelect,
  cancelButton,
  controlButtons,
  dateTime,
  form,
  saveButton,
  scheduleAddModal,
};
