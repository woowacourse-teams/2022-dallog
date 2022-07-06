import { css, Theme } from '@emotion/react';

const scheduleAddModal = ({ colors }: Theme) => css`
  width: 600px;
  height: 600px;
  padding: 50px;
  border-radius: 12px;

  background: ${colors.WHITE};
`;

const form = ({ flex }: Theme) => css`
  ${flex.column};

  height: 100%;
  justify-content: space-between;
`;

const allDayButton = ({ colors }: Theme) => css`
  width: 500px;
  height: 36px;
  border: 1px solid ${colors.GRAY_500};
  border-radius: 8px;
  filter: drop-shadow(0px 2px 2px ${colors.GRAY_400});

  background: ${colors.WHITE};

  font-size: 20px;
  color: ${colors.GRAY_600};
`;

const dateTime = ({ flex }: Theme) => css`
  ${flex.column}

  gap: 10px;
`;

const arrow = ({ colors }: Theme) => css`
  font-size: 24px;
  font-weight: bold;
  color: ${colors.GRAY_500};
`;

const controlButtons = ({ flex }: Theme) => css`
  ${flex.row}

  align-self: flex-end;
  gap: 20px;
`;

const cancelButton = ({ colors }: Theme) => css`
  width: 90px;
  height: 40px;
  border: 2px solid ${colors.GRAY_500};
  border-radius: 8px;
  filter: drop-shadow(0px 2px 2px ${colors.GRAY_400});

  background: ${colors.WHITE};

  font-size: 20px;
  color: ${colors.GRAY_600};
`;

const saveButton = ({ colors }: Theme) => css`
  width: 90px;
  height: 40px;
  border-radius: 8px;
  filter: drop-shadow(0px 2px 2px ${colors.GRAY_400});

  background: ${colors.YELLOW_500};

  font-size: 20px;
  color: ${colors.WHITE};
`;

export {
  allDayButton,
  arrow,
  cancelButton,
  controlButtons,
  dateTime,
  form,
  saveButton,
  scheduleAddModal,
};
