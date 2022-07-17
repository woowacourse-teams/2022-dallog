import { css, Theme } from '@emotion/react';

const categoryAddModal = ({ colors, flex }: Theme) => css`
  ${flex.column}

  width: 120rem;
  height: 160rem;
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

export { cancelButton, categoryAddModal, content, controlButtons, form, saveButton, title };
