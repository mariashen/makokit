class AddLinkListToInstruction < ActiveRecord::Migration
  def change
    add_column :instructions, :next_instruction_id, :integer
    remove_column :instructions, :display_position
  end
end
