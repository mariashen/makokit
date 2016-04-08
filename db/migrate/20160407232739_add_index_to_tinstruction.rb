class AddIndexToTinstruction < ActiveRecord::Migration
  def change
    add_column :instructions, :display_index, :integer
  end
end
