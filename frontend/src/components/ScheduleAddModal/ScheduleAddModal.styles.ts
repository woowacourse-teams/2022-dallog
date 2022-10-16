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

  input + label {
    position: relative;

    width: 4rem;
    height: 4rem;
    border: 1px solid ${colors.YELLOW_500};
    border-radius: 2px;

    &:hover {
      cursor: pointer;
    }
  }

  input:checked + label::after {
    content: '✓';

    position: absolute;
    top: -1px;
    left: -1px;

    width: 4rem;
    height: 4rem;
    border-radius: 2px;

    background: ${colors.YELLOW_500};

    font-weight: 600;
    color: white;
    text-align: center;
  }

  input {
    display: none;
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
  box-shadow: 0 2px 2px ${colors.GRAY_400};

  background: ${colors.WHITE};

  font-size: 4rem;
  color: ${colors.GRAY_600};
`;

const saveButton = ({ colors }: Theme) => css`
  padding: 2rem 3rem;
  box-sizing: border-box;
  border-radius: 8px;
  box-shadow: 0 2px 2px ${colors.GRAY_400};

  background: ${colors.YELLOW_500};

  font-size: 4rem;
  color: ${colors.WHITE};
`;

const labelStyle = ({ colors }: Theme) => css`
  padding: 0 1rem;

  font-size: 4rem;
  color: ${colors.GRAY_800};
`;

const dateTimePickerStyle = ({ flex }: Theme) => css`
  ${flex.row};

  justify-content: space-between;
  align-items: flex-end;

  width: 100%;
`;

const dateFieldsetStyle = (isAllDay: boolean) => {
  return {
    div: css`
      width: ${isAllDay ? '100%' : '45%'};
    `,
    input: css`
      height: 11.75rem;
    `,
  };
};

const selectTimeStyle = {
  select: css`
    width: 45%;
  `,
};

export {
  arrow,
  cancelButton,
  checkboxStyle,
  controlButtons,
  dateFieldsetStyle,
  dateTime,
  dateTimePickerStyle,
  form,
  labelStyle,
  saveButton,
  scheduleAddModal,
  selectBoxStyle,
  selectTimeStyle,
};
