import { css, Theme } from '@emotion/react';

const categoryAddModal = ({ colors, flex }: Theme) => css`
  ${flex.column}

  width: 120rem;
  height: 90rem;
  padding: 12.5rem;
  border-radius: 12px;
  justify-content: space-between;

  background: ${colors.WHITE};
`;

const title = ({ colors }: Theme) => css`
  font-size: 8rem;
  font-weight: bold;
  color: ${colors.GRAY_700};
`;

const form = ({ flex }: Theme) => css`
  ${flex.column};

  width: 100%;
  height: 100%;
  justify-content: space-between;
`;

const content = ({ flex }: Theme) => css`
  ${flex.column};

  width: 100%;
  height: 100%;

  justify-content: center;
`;

const controlButtons = ({ flex }: Theme) => css`
  ${flex.row}

  align-self: flex-end;
  gap: 5rem;
`;

const cancelButtonStyle = ({ colors }: Theme) => css`
  padding: 2rem 3rem;
  box-sizing: border-box;
  border: 1px solid ${colors.GRAY_500};
  border-radius: 8px;
  filter: drop-shadow(0 2px 2px ${colors.GRAY_400});

  background: ${colors.WHITE};

  font-size: 4rem;
  color: ${colors.GRAY_600};
`;

const saveButtonStyle = ({ colors }: Theme) => css`
  padding: 2rem 3rem;
  box-sizing: border-box;
  border-radius: 8px;
  filter: drop-shadow(0px 2px 2px ${colors.GRAY_400});

  background: ${colors.YELLOW_500};

  font-size: 4rem;
  color: ${colors.WHITE};
`;

export {
  cancelButtonStyle,
  categoryAddModal,
  content,
  controlButtons,
  form,
  saveButtonStyle,
  title,
};
