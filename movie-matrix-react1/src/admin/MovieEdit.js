import React from "react";
import { Edit, SimpleForm, TextInput, DateInput } from "react-admin";

const MovieEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="title" />
      <TextInput source="genre" />
      <TextInput source="director" />
      <DateInput source="releaseDate" />
    </SimpleForm>
  </Edit>
);

export default MovieEdit;
