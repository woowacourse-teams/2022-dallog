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

const dateTime = ({ flex }: Theme) => css`
  ${flex.column}

  position: relative;
  gap: 2.5rem;

  width: 100%;
`;

const checkboxStyle = ({ colors, flex }: Theme) => css`
  ${flex.row}

  position: absolute;
  top: 0;
  right: 1rem;
  gap: 2rem;

  font-size: 4rem;
  color: ${colors.GRAY_700};

  input {
    accent-color: ${colors.ORANGE_500};
    transform: scale(1.1);
  }
`;

const arrow = ({ colors }: Theme) => css`
  font-size: 6rem;
  font-weight: bold;
  color: ${colors.GRAY_500};
`;

const selectBoxStyle = ({ flex }: Theme) => css`
  ${flex.column};

  align-items: flex-start;
  gap: 2.5rem;

  width: 100%;
`;

const categorySelect = ({ colors }: Theme) => css`
  width: 100%;
  padding: 3rem;
  border: 1px solid ${colors.GRAY_400};
  border-radius: 8px;

  font-size: 4rem;
`;

const controlButtons = ({ flex }: Theme) => css`
  ${flex.row}

  align-self: flex-end;
  gap: 5rem;
`;

const cancelButton = ({ colors }: Theme) => css`
  padding: 2rem 3rem;
  box-sizing: border-box;
  border: 1px solid ${colors.GRAY_500};
  border-radius: 8px;
  filter: drop-shadow(0 2px 2px ${colors.GRAY_400});

  background: ${colors.WHITE};

  font-size: 4rem;
  color: ${colors.GRAY_600};
`;

const saveButton = ({ colors }: Theme) => css`
  padding: 2rem 3rem;
  box-sizing: border-box;
  border-radius: 8px;

  filter: drop-shadow(0px 2px 2px ${colors.GRAY_400});

  background: ${colors.YELLOW_500};

  font-size: 4rem;
  color: ${colors.WHITE};
`;

const labelStyle = ({ colors }: Theme) => css`
  padding: 0 1rem;

  font-size: 4rem;
  color: ${colors.GRAY_800};
`;

export {
  arrow,
  categorySelect,
  cancelButton,
  checkboxStyle,
  controlButtons,
  dateTime,
  form,
  labelStyle,
  saveButton,
  scheduleAddModal,
  selectBoxStyle,
};
