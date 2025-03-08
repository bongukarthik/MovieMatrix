import React from "react";
import { List, Datagrid, TextField, EditButton, DeleteButton, Edit, SimpleForm, TextInput, Create } from "react-admin";

// List View
export const MovieList = (props) => (
    <List {...props}>
        <Datagrid>
            <TextField source="id" />
            <TextField source="title" />
            <TextField source="genre" />
            <TextField source="director" />
            <TextField source="releaseDate" />
            <EditButton />
            <DeleteButton />
        </Datagrid>
    </List>
);

// Create Movie
export const MovieCreate = (props) => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source="title" />
            <TextInput source="genre" />
            <TextInput source="director" />
            <TextInput source="releaseDate" />
            <TextInput source="description" multiline />
            <TextInput source="rating" />
        </SimpleForm>
    </Create>
);

// Edit Movie
export const MovieEdit = (props) => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput source="title" />
            <TextInput source="genre" />
            <TextInput source="director" />
            <TextInput source="releaseDate" />
            <TextInput source="description" multiline />
            <TextInput source="rating" />
        </SimpleForm>
    </Edit>
);
